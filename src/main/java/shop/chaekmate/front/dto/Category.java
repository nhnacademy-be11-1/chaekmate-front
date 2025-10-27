package shop.chaekmate.front.dto;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private long id;
    private String name;
    private List<Category> children = new ArrayList<>();

    public Category(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public List<Category> getChildren() { return children; }

    public void addChild(Category child) {
        this.children.add(child);
    }
}
