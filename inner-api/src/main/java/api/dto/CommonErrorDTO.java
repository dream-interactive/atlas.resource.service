package api.dto;

public class CommonErrorDTO {
    private Long ceid; //common error id
    private String code; // ATLAS, JDK, SQL
    private String section; // project, organization, ticket etc
    private String title;
    private String description; // description for programmer
    private boolean isPrivate;
}
