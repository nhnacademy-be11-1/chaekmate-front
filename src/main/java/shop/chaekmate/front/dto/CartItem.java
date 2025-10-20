package shop.chaekmate.front.dto;

public record CartItem(Book book, int quantity) {
    public double getSubtotal() {
        return book.price() * quantity;
    }
}
