package shop.chaekmate.front.book.entity;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class Category {
    private long id;
    private String name;
    private List<Category> children = new ArrayList<>();

    public Category(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addChild(Category child) {
        this.children.add(child);
    }
}
