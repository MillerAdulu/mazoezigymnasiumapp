package ke.co.milleradulu.milleradulu.mazoezigymnasium.clients;

import java.util.List;

import ke.co.milleradulu.milleradulu.mazoezigymnasium.models.Member;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MemberClient {
    @GET ("/member")
    Call<List<Member>> members();

    @GET ("/member/profile/{id}")
    Call<Member> member(
            @Path("id") int id
    );

    @FormUrlEncoded
    @POST ("/member/extradetails/{id}")
    Call<Member> update(
            @Path("id") int id,
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("email") String email,
            @Field("home") int home,
            @Field("age") int age,
            @Field("gender") int gender,
            @Field("weight") float weight,
            @Field("target_weight") float target_weight
    );

    @FormUrlEncoded
    @POST ("/member/login")
    Call<Member> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST ("/member/register")
    Call<Member> register(
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("email") String email,
            @Field("password") String password
    );
}
