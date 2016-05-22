import java.io.File

import org.apache.commons.io.FileUtils
import spray.json.{DefaultJsonProtocol, JsValue, RootJsonFormat}
import todos.Todo

/**
  * Created by midori on 2016/05/22.
  */
object Main{
  def main(args: Array[String]) {
    case class TodoList(items: Seq[Todo]){
      def addTodo(todo: Todo):  Seq[Todo] ={
        items :+ todo
      }

      def addTodo(todoList: Seq[Todo]) : Seq[Todo] = {
          items ++ todoList
      }
    }

    object Todo{
      def apply(title: String, content: String) : Todo = new Todo(title, content)
    }

    var todoList : Seq[Todo] = Nil


    object MyJsonProtocol extends DefaultJsonProtocol{
      implicit val todoFormat = jsonFormat2(Todo.apply)
      implicit object TodoListJsonFormat extends RootJsonFormat[TodoList]{
        def read(value: JsValue) = TodoList(value.convertTo[List[Todo]])
        def write(f: TodoList) = ???
      }
    }

    import MyJsonProtocol._
    import spray.json._
    val jsonStr = FileUtils.readFileToString(new File("data/data.json"), "UTF-8")
    val json = jsonStr.parseJson
    val initialTodoList = json.convertTo[TodoList]


    val todo3 = Todo.apply("3つめ", "ちゃんと毎日すすめること")
    todoList = initialTodoList.addTodo(todo3)
    println(todoList)

    val result = todoList.toJson
    val resultJson = result.prettyPrint

    FileUtils.writeStringToFile(new File("data/data.json"), resultJson, "UTF-8")

  }
}
