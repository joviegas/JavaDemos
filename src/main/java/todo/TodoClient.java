package todo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class TodoClient {

    private final String BASE_URL = "https://jsonplaceholder.typicode.com/todos";
    private final HttpClient client;

    private final ObjectMapper objectMapper ;

    public TodoClient( ) {
        this.client = HttpClient.newBuilder()
                .build();
        objectMapper = new ObjectMapper();
    }

    public List<Todo> finaAll() throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();

        HttpResponse<String> response = this.client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(response.body(), new TypeReference<List<Todo>>() {});
    }

    public Todo findById(Integer id) throws IOException, InterruptedException, TodoNotFoundException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .GET()
                .build();

        HttpResponse<String> response = this.client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if(response.statusCode() == 404) {
            throw new TodoNotFoundException("todo.Todo not found");

        };
        return objectMapper.readValue(response.body(), Todo.class);
    }


    public HttpResponse<String> create(Todo todo) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(todo)))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());

    }

    public HttpResponse<String> update(Todo todo)throws IOException, InterruptedException{
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL+"/"+todo.id()))
                .PUT(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(todo)))
                .build();

        return client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }


    public HttpResponse<String> delete(Todo todo) throws IOException, InterruptedException{
        var request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL+"/"+todo.id()))
                .DELETE()
                .build();

        return client.send(request,HttpResponse.BodyHandlers.ofString());
    }


}
