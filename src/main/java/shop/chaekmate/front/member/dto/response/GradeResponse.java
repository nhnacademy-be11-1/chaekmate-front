package shop.chaekmate.front.member.dto.response;

public record GradeResponse(
        String name,
        Byte pointRate,
        int upgradeStandardAmount
) {
}