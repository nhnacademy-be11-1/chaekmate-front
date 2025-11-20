package shop.chaekmate.front.member.dto.request;

public record UpdateGradeRequest(
        String name,
        Byte pointRate,
        int upgradeStandardAmount
) {
}