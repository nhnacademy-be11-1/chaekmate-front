package shop.chaekmate.front.member.dto.response;

public record GradeResponse(
        Long id,
        String name,
        Byte pointRate,
        int upgradeStandardAmount
) {
}