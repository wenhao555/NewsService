package com.demo.springboot_vue.controller;

import com.demo.springboot_vue.entities.News;
import com.demo.springboot_vue.entities.Recommend;
import com.demo.springboot_vue.entities.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class JdbcController {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 初始化user数据
     */
    @RequestMapping("/initUser")
    public void init() {
        User user = new User();
        for (int count = 0; count < 10; count++) {
            user.setAccount("user" + count);
            user.setPassword("user" + count);
            user.setBirth("2000-10-10 10:10:10");
            user.setAdmin(false);
            mongoTemplate.insert(user);
        }
        user.setAccount("admin");
        user.setPassword("admin");
        user.setBirth("2000-10-10 10:10:10");
        user.setAdmin(true);
        mongoTemplate.insert(user);
    }

    /**
     * 登录
     *
     * @param appUser
     * @return
     */
    @RequestMapping("/login")
    public User login(@RequestBody User appUser) {
        List<User> userList = mongoTemplate.find(new Query(Criteria.where("account").is(appUser.getAccount())), User.class);
        if (userList.size() == 0 || !appUser.getPassword().equals(userList.get(0).getPassword())) {
            return null;
        }
        return userList.get(0);
    }

    /**
     * admin登录
     *
     * @param appUser
     * @return
     */
    @RequestMapping("/adminLogin")
    public String adminLogin(@RequestBody User appUser) {
        List<User> userList = mongoTemplate.find(new Query(Criteria.where("account").is(appUser.getAccount())), User.class);
        String returnMsg = "";
        if (userList.size() == 0) {
            returnMsg = "没有该用户，请确认账户";
        }
        if (appUser.getPassword().equals(userList.get(0).getPassword()) && userList.get(0).getAdmin() == true) {
            returnMsg = "登陆成功";
        }
        return returnMsg;
    }

    /**
     * 注册用户
     *
     * @param appUser
     * @return
     */
    @RequestMapping("/createUser")
    public Boolean createUser(@RequestBody User appUser) {
        mongoTemplate.insert(appUser);
        return true;
    }

    /**
     * 修改密码
     *
     * @param appUser
     * @return
     */
    @RequestMapping("/modifyPass")
    public Boolean modifyPass(@RequestBody User appUser) {
        Query query = new Query(Criteria.where("account").is(appUser.getAccount()));
        Update password = Update.update("password", appUser.getPassword());
        mongoTemplate.upsert(query, password, User.class);
        return true;
    }

    /**
     * 获取全部用户信息
     *
     * @return
     */
    @RequestMapping("/getAllUserInfo")
    public List<User> getAllUserInfo() {
        return mongoTemplate.findAll(User.class);
    }

    /**
     * 获取用户信息
     *
     * @param appUser
     * @return
     */
    @RequestMapping("/getUserInfo")
    public User getUserInfo(@RequestBody User appUser) {
        List<User> userList = mongoTemplate.find(new Query(Criteria.where("account").is(appUser.getAccount())), User.class);
        return userList.get(0);
    }

    /**
     *
     */
    @RequestMapping("/removeUser")
    public Boolean removeUser(@RequestBody User appUser) {
        mongoTemplate.remove(new Query(Criteria.where("account").is(appUser.getAccount())), User.class);
        return true;
    }

    /**
     * 管理员更改用户信息
     *
     * @param appUser
     * @return
     */
    @RequestMapping("/setUserInfo")
    public User setUserInfo(@RequestBody User appUser) {
        Query query = new Query(Criteria.where("account").is(appUser.getAccount()));
        Update update = Update.update("name", appUser.getName());
        mongoTemplate.upsert(query, update, User.class);
        update = Update.update("sex", appUser.getSex());
        mongoTemplate.upsert(query, update, User.class);
        update = Update.update("password", appUser.getPassword());
        mongoTemplate.upsert(query, update, User.class);
        update = Update.update("image", appUser.getImage());
        mongoTemplate.upsert(query, update, User.class);
        return appUser;
    }

    /**
     * 查看所有新闻信息（除推荐）
     *
     * @param
     * @return
     */
    @RequestMapping("/getNews")
    public List<News> getNews() {
        return mongoTemplate.findAll(News.class);
    }

    /**
     * 根据类别查询新闻信息（除推荐）
     *
     * @return
     */
    @RequestMapping("/getNewsByType")
    public List<News> getNewsByType(@RequestBody News news) {
        return mongoTemplate.find(new Query(Criteria.where("type").is(news.getType())), News.class);
    }

    /**
     * 根据类别查询新闻信息（推荐）
     *
     * @return
     */
    @RequestMapping("/getNewsByRecommend")
    public List<Recommend> getNewsByRecommend() {
        return mongoTemplate.findAll(Recommend.class);
    }

    /**
     * 设置新闻为推荐新闻
     *
     * @return
     */
    @RequestMapping("/setNewsToRecommend")
    public String setNewsToRecommend(@RequestBody News news) {
        if (mongoTemplate.find(new Query(Criteria.where("id").is(news.getId())), Recommend.class).size() > 0) {
            return "已存在";
        }
        Recommend recommend = new Recommend();
        BeanUtils.copyProperties(news, recommend);
        mongoTemplate.insert(recommend);
        return "推荐成功";
    }

    /**
     * 管理员输入新闻
     *
     * @param news
     * @return
     */
    @RequestMapping("/setNews")
    public Boolean setNews(@RequestBody News news) {
        int size = mongoTemplate.findAll(News.class).size();
        news.setId(size + 1);
        mongoTemplate.insert(news);
        return true;
    }

    /**
     * 管理员删除新闻
     *
     * @param news
     * @return
     */
    @RequestMapping("/removeNews")
    public Boolean removeNews(@RequestBody News news) {
        mongoTemplate.remove(new Query(Criteria.where("id").is(news.getId())), News.class);
        mongoTemplate.remove(new Query(Criteria.where("id").is(news.getId())), Recommend.class);
        return true;
    }

    /**
     * 管理员更改新闻
     *
     * @param news
     * @return
     */
    @RequestMapping("/modifyNews")
    public News modifyNews(@RequestBody News news) {
        Query query = new Query(Criteria.where("id").is(news.getId()));
        Update update = Update.update("content", news.getContent());
        mongoTemplate.upsert(query, update, News.class);
        update = Update.update("image", news.getImage());
        mongoTemplate.upsert(query, update, News.class);
        update = Update.update("type", news.getType());
        mongoTemplate.upsert(query, update, News.class);
        update = Update.update("title", news.getTitle());
        mongoTemplate.upsert(query, update, News.class);
        List<Recommend> recommendList = mongoTemplate.find(query, Recommend.class);
        if (recommendList.size() > 0) {
            update = Update.update("content", news.getContent());
            mongoTemplate.upsert(query, update, Recommend.class);
            update = Update.update("image", news.getImage());
            mongoTemplate.upsert(query, update, Recommend.class);
            update = Update.update("type", news.getType());
            mongoTemplate.upsert(query, update, Recommend.class);
            update = Update.update("title", news.getTitle());
            mongoTemplate.upsert(query, update, News.class);
        }
        return news;
    }

    /**
     * 管理员删除推荐新闻
     *
     * @param recommend
     * @return
     */
    @RequestMapping("/removeRecommend")
    public Boolean removeRecommend(@RequestBody Recommend recommend) {
        mongoTemplate.remove(new Query(Criteria.where("id").is(recommend.getId())), Recommend.class);
        return true;
    }

}
