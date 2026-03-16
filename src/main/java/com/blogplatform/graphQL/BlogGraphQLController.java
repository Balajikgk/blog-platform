package com.blogplatform.graphQL;

import com.blogplatform.entity.Comment;
import com.blogplatform.entity.Post;
import com.blogplatform.repository.CommentRepository;
import com.blogplatform.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BlogGraphQLController {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @QueryMapping
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }


    @QueryMapping
    public Post getPostById(@Argument Long id) {
        return postRepository.findById(id).orElse(null);
    }


    @MutationMapping
    public Post createPost(@Argument String title,
                           @Argument String content) {

        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);

        return postRepository.save(post);
    }

    @MutationMapping
    public Comment addComment(@Argument Long postId,
                              @Argument String message) {

        Post post = postRepository.findById(postId).orElseThrow();

        Comment comment = new Comment();
        comment.setContent(message);
        comment.setPost(post);

        return commentRepository.save(comment);
    }
}
