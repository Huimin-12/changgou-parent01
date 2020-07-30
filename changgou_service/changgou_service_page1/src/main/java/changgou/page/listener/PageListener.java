package changgou.page.listener;

import changgou.page.config.RabbitMQconfig;
import changgou.page.service.PageService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PageListener {

    @Autowired
    private PageService pageService;
    //声明交换机名称
    @RabbitListener(queues = RabbitMQconfig.PAGE_CATEGORY_QUEUE)
    public void receiveMessage(String spuId){
        System.out.println("生成商品详情页面，id为："+spuId);

        pageService.generateItemPage(spuId);
    }
}
