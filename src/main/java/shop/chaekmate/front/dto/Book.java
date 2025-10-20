package shop.chaekmate.front.dto;

import java.util.List;

public record Book(long id, String title, String author, double price, String imageUrl, String description, long categoryId, List<Long> tagIds) {}
