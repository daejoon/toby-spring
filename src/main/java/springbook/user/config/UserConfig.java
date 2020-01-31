package springbook.user.config;

import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.transaction.PlatformTransactionManager;
import springbook.learningtest.factory.MessageFactoryBean;
import springbook.user.dao.*;
import springbook.user.service.DummyMailSender;
import springbook.user.service.TransactionAdvice;
import springbook.user.service.UserServiceImpl;

import javax.sql.DataSource;

@Configuration
public class UserConfig {

    @Bean
    public UserDao userDao() {
        UserDaoImpl userDao = new UserDaoImpl();
        userDao.setDataSource(dataSource());
        return userDao;
    }

//    @Bean("userService")
//    public TxProxyFactoryBean userServiceTx() {
//        TxProxyFactoryBean txProxyFactoryBean = new TxProxyFactoryBean();
//        txProxyFactoryBean.setTarget(userService());
//        txProxyFactoryBean.setTransactionManager(platformTransactionManager());
//        txProxyFactoryBean.setPattern("upgradeLevels");
//        txProxyFactoryBean.setServiceInstance(UserService.class);
//        return txProxyFactoryBean;
//    }

    @Bean("userService")
    public ProxyFactoryBean userServicePfb() {
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(userService());
        proxyFactoryBean.addAdvisor(defaultPointcutAdvisor());
        return proxyFactoryBean;
    }

    @Bean("userServiceImpl")
    public UserServiceImpl userService() {
        UserServiceImpl userService = new UserServiceImpl();
        userService.setUserDao(userDao());
        userService.setMailSender(mailSender());
        return userService;
    }

    @Bean
    public ConnectionMaker connectionMaker() {

        return new CountingConnectionMaker(realConnectionMaker());
    }

    @Bean
    public ConnectionMaker realConnectionMaker() {

        return new DConnectionMaker();
    }

    @Bean
    public DataSource dataSource() {

        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost:3307/springbook");
        dataSource.setUsername("spring");
        dataSource.setPassword("spring");
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public MailSender mailSender() {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setHost("mail.server.com");
//        return mailSender;
        return new DummyMailSender();
    }

    @Bean("message")
    public MessageFactoryBean messageFactoryBean() {
        MessageFactoryBean messageFactoryBean = new MessageFactoryBean();
        messageFactoryBean.setText("Factory Bean");
        return messageFactoryBean;
    }

    @Bean
    public TransactionAdvice transactionAdvice() {
        TransactionAdvice transactionAdvice = new TransactionAdvice();
        transactionAdvice.setTransactionManager(platformTransactionManager());
        return transactionAdvice;
    }

    @Bean("transactionPointcut")
    public NameMatchMethodPointcut nameMatchMethodPointcut() {
        NameMatchMethodPointcut nameMatchMethodPointcut = new NameMatchMethodPointcut();
        nameMatchMethodPointcut.setMappedName("upgrade*");
        return nameMatchMethodPointcut;
    }

    @Bean("transactionAdvisor")
    public DefaultPointcutAdvisor defaultPointcutAdvisor() {
        DefaultPointcutAdvisor defaultPointcutAdvisor = new DefaultPointcutAdvisor();
        defaultPointcutAdvisor.setAdvice(transactionAdvice());
        defaultPointcutAdvisor.setPointcut(nameMatchMethodPointcut());
        return defaultPointcutAdvisor;
    }
}
