package shop.chaekmate.front.book.entity;

import lombok.Getter;

@Getter
public class Tag {
    private long id;
    private String name;

    public Tag(long id, String name) {
        this.id = id;
        this.name = name;
    }

}
