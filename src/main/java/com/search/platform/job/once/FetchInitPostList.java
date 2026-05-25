package com.search.platform.job.once;

import com.search.platform.model.entity.Post;
import com.search.platform.service.PostService;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;

/**
 * 本地初始化示例帖子数据。
 */
//@Component
@Slf4j
public class FetchInitPostList implements CommandLineRunner {

    @Resource
    private PostService postService;

    @Override
    public void run(String... args) {
        List<Post> postList = new ArrayList<>();

        Post post1 = new Post();
        post1.setTitle("Spring Boot 聚合搜索示例");
        post1.setContent("这是一个用于本地学习的初始化帖子，演示文章数据如何进入统一搜索流程。");
        post1.setTags("[\"Spring Boot\",\"Search\"]");
        post1.setUserId(1L);
        postList.add(post1);

        Post post2 = new Post();
        post2.setTitle("图片搜索数据源说明");
        post2.setContent("该示例用于说明图片搜索数据的适配和统一返回结构。");
        post2.setTags("[\"DataSource\",\"Image\"]");
        post2.setUserId(1L);
        postList.add(post2);

        boolean success = postService.saveBatch(postList);
        if (success) {
            log.info("初始化示例帖子成功，条数 = {}", postList.size());
        } else {
            log.error("初始化示例帖子失败");
        }
    }
}
