package com.itcatcetc.smarthome.login;


/*@Entity
@Table(name="role")*/
public record Role() {
   /* @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;
    private String name;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    }*/

    public static final String HOMIE = "HOMIE";
    public  static final String GUEST = "GUEST";
}
