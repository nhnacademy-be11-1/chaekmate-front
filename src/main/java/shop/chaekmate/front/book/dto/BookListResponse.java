package shop.chaekmate.front.book.dto;

public record BookListResponse(
        Long id,
        String title,
        String author,
        String publisher,
        int price,
        int salesPrice,
        String imageUrl
) {
    // 할인율 계산
    public int discountRate() {
        if (price == 0 || salesPrice >= price) return 0;
        return (int) (((double) (price - salesPrice) / price) * 100);
    }

    // 할인 여부
    public boolean isOnSale() {
        return salesPrice < price;
    }

    // 할인 금액
    public int discountAmount() {
        return price - salesPrice;
    }
}
