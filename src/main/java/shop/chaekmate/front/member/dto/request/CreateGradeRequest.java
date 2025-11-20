package shop.chaekmate.front.member.dto.request;

public record CreateGradeRequest(
        String name,
        Byte pointRate,
        int upgradeStandardAmount
) {
}