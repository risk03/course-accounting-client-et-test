package bsuir.filipovich.accountingclient;

import bsuir.filipovich.accountingclient.service.IService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

@Configuration
public class Config {
    @Bean
    RmiProxyFactoryBean rmiProxy() {
        RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
        bean.setServiceInterface(IService.class);
        bean.setServiceUrl("rmi://localhost:1099/accountingservice");
        return bean;
    }
}
