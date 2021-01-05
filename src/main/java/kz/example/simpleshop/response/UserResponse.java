package kz.example.simpleshop.response;

public class UserResponse<Model,Response> {
    private Model user = null;
    private Response response;

    public Model getUser() {
        return user;
    }

    public void setUser(Model user) {
        this.user = user;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
