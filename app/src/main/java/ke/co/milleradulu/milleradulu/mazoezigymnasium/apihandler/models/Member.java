package ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.models;

public class Member {
  private String memberFirstName, memberLastName, memberEmail, memberHome;
  private int memberAge, memberGender, memberId;
  private float memberWeight, memberTargetWeight;

  public int getMemberId() {
    return memberId;
  }

  public void setMemberId(int memberId) {
    this.memberId = memberId;
  }

  public String getMemberFirstName() {
    return memberFirstName;
  }

  public void setMemberFirstName(String memberFirstName) {
    this.memberFirstName = memberFirstName;
  }

  public String getMemberLastName() {
    return memberLastName;
  }

  public void setMemberLastName(String memberLastName) {
    this.memberLastName = memberLastName;
  }

  public String getMemberEmail() {
    return memberEmail;
  }

  public void setMemberEmail(String memberEmail) {
    this.memberEmail = memberEmail;
  }

  public String getMemberHome() {
    return memberHome;
  }

  public void setMemberHome(String memberHome) {
    this.memberHome = memberHome;
  }

  public int getMemberAge() {
    return memberAge;
  }

  public void setMemberAge(int memberAge) {
    this.memberAge = memberAge;
  }

  public int getMemberGender() {
    return memberGender;
  }

  public void setMemberGender(int memberGender) {
    this.memberGender = memberGender;
  }

  public float getMemberWeight() {
    return memberWeight;
  }

  public void setMemberWeight(float memberWeight) {
    this.memberWeight = memberWeight;
  }

  public float getMemberTargetWeight() {
    return memberTargetWeight;
  }

  public void setMemberTargetWeight(float memberTargetWeight) {
    this.memberTargetWeight = memberTargetWeight;
  }
}
