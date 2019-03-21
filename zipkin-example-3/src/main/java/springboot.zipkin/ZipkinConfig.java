package springboot.zipkin;

import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.EmptySpanCollectorMetricsHandler;
import com.github.kristofa.brave.Sampler;
import com.github.kristofa.brave.SpanCollector;
import com.github.kristofa.brave.http.DefaultSpanNameProvider;
import com.github.kristofa.brave.http.HttpSpanCollector;
import com.github.kristofa.brave.httpclient.BraveHttpRequestInterceptor;
import com.github.kristofa.brave.httpclient.BraveHttpResponseInterceptor;
import com.github.kristofa.brave.servlet.BraveServletFilter;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: 程泰恒
 * @date: 2019/3/19 13:40
 */
@Configuration
public class ZipkinConfig {

    /**
     * 配置处理器
     *
     * @return
     */
    @Bean
    public SpanCollector configSpanClollector() {
        HttpSpanCollector.Config config = HttpSpanCollector.Config.builder().compressionEnabled(false).connectTimeout(5000).flushInterval(1).readTimeout(6000).build();
        return HttpSpanCollector.create("http://192.168.80.131:9411", config, new EmptySpanCollectorMetricsHandler());
    }

    /**
     * brave各种工具类的封装
     *
     * @param spanCollector
     * @return
     */
    @Bean
    public Brave brave(SpanCollector spanCollector) {
        Brave.Builder builder = new Brave.Builder("service3");//指定serviceName
        builder.spanCollector(spanCollector);
        builder.traceSampler(Sampler.create(1));//采集率

        return builder.build();
    }

    /**
     * 拦截器，需要serverRequestInterceptor和serverResponseInterceptor 分别完成sr和ss操作
     *
     * @param brave
     * @return
     */
    @Bean
    public BraveServletFilter setBraveServletFilter(Brave brave) {
        return new BraveServletFilter(brave.serverRequestInterceptor(), brave.serverResponseInterceptor(), new DefaultSpanNameProvider());
    }

    /**
     *httpclent客户端，需要clientRequestInterceptor和clientResponseInterceptor分别完成cs和cr操作
     * @param brave
     * @return
     */
    @Bean
    public CloseableHttpClient getcloseableHttpClient(Brave brave) {
        CloseableHttpClient httpClient = HttpClients.custom()
                .addInterceptorFirst(new BraveHttpRequestInterceptor(brave.clientRequestInterceptor(), new DefaultSpanNameProvider()))
                .addInterceptorFirst(new BraveHttpResponseInterceptor(brave.clientResponseInterceptor())).build();
        return httpClient;
    }

}
