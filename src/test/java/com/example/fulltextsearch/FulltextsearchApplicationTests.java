package com.example.fulltextsearch;

import com.example.fulltextsearch.service.ArticleService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

@SpringBootTest
class FulltextsearchApplicationTests {

	@Resource
	ArticleService articleService;

	@Test
	void contextLoads() {
	}

	@Test
	void insert() throws IOException {
		Files.walkFileTree(Path.of("/Users/a58/Downloads/THUCNews/data/THUCNews"), new SimpleFileVisitor<>(){
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				System.out.println(file.toAbsolutePath());
				if (file.toAbsolutePath().toString().endsWith(".txt")) {
					List<String> lines = Files.readAllLines(file);
					String title = lines.get(0);
					String content = String.join("\n", lines.subList(1, lines.size() - 1));
					articleService.createArticle(title, content);
				}
				return super.visitFile(file, attrs);
			}
		});

	}

}
