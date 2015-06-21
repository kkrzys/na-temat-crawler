package main.java.pl.edu.agh.toik.crawler;

import main.java.pl.edu.agh.toik.mail_notification.NaTematCrawlerMailNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class NaTematCrawlerStatistic implements Runnable {

    @Autowired
    private Environment env;

    @Autowired
    private NaTematCrawlerMailNotification naTematCrawlerMailNotification;

    private volatile boolean isRunning;

    private Integer allArticlesCrawled = 0;
    private Integer allCommentsCrawled = 0;
    private Integer allSubCommentsCrawled = 0;

    public Integer getAllArticlesCrawled() {
        return allArticlesCrawled;
    }

    public void setAllArticlesCrawled(Integer allArticlesCrawled) {
        this.allArticlesCrawled = allArticlesCrawled;
    }

    public Integer getAllCommentsCrawled() {
        return allCommentsCrawled;
    }

    public void setAllCommentsCrawled(Integer allCommentsCrawled) {
        this.allCommentsCrawled = allCommentsCrawled;
    }

    public Integer getAllSubCommentsCrawled() {
        return allSubCommentsCrawled;
    }

    public void setAllSubCommentsCrawled(Integer allSubCommentsCrawled) {
        this.allSubCommentsCrawled = allSubCommentsCrawled;
    }

    @Override
    public void run() {

        long startTime = System.currentTimeMillis();
        long time = 0l;

        Integer frequency = Integer.valueOf(env.getRequiredProperty("mail.time"));

        this.isRunning = true;

        while (this.isRunning) {

            time = System.currentTimeMillis() - startTime;

            try {
                if (time % frequency >= 0 && time % frequency <= 2000) {
                    naTematCrawlerMailNotification.getMailNotificationService().sendMailNotification(
                            "NaTematCrawler statistic",
                            "Time: " + new Date() + " (" + time + "[ms])" + "\n" +
                                    "All articles crawled: " + allArticlesCrawled + "\n" +
                                    "All comments crawled: " + allCommentsCrawled + "\n" +
                                    "All subcomments crawled: " + allSubCommentsCrawled + "\n"
                    );
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void terminate() {
        this.isRunning = false;
    }

}