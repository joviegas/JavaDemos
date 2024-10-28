package todo;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TodoClientTest {

    TodoClient client = new TodoClient();

    @Test
    void finaAll() throws IOException, InterruptedException {
        List<Todo> todoList = client.finaAll();
        assertEquals(200, todoList.size());
    }

    @Test
    void shouldReturnTodoGivenValidId() throws IOException, InterruptedException, TodoNotFoundException {
        Todo todo = client.findById(1);
        assertEquals(1, todo.userId());
        assertEquals("1", todo.id());
        assertEquals("delectus aut autem", todo.title());
        assertFalse(todo.completed());
    }

    @Test
    void shouldThrowTodoNotFoundExceptionGivenInvalidId() {
        assertThrows(TodoNotFoundException.class, () -> client.findById(999));
    }

    @Test
    void shouldCreateNewTodo() throws IOException, InterruptedException, TodoNotFoundException {
        Todo todo = new Todo(201,"1","Learn Java",false);
        HttpResponse<String> response = client.create(todo);
        assertEquals(201, response.statusCode());
    }

    @Test
    void shouldUpdateExistingTodo() throws IOException, InterruptedException, TodoNotFoundException {
        Todo todo = new Todo(1,"1","NEW TITLE", true);
        HttpResponse<String> response = client.update(todo);
        assertEquals(200, response.statusCode());
    }

    @Test
    void shouldDeleteExistingTodo() throws IOException, InterruptedException, TodoNotFoundException {
        Todo todo = client.findById(1);
        HttpResponse<String> response = client.delete(todo);
        assertEquals(200, response.statusCode());
    }


}