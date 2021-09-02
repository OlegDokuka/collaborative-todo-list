@file:Suppress("unused")

package todomvcfx.tornadofx.models

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue

class TodoItem(val id: Uuid = uuid4(), text: String, completed: Boolean = false) {

    val textProperty = SimpleStringProperty(text)
    var text by textProperty

    val completedProperty = SimpleBooleanProperty(completed)
    var completed by completedProperty

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as TodoItem

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

class TodoItemModel(property: ObjectProperty<TodoItem>) :
    ItemViewModel<TodoItem>(itemProperty = property) {
    val text = bind(autocommit = true) {
        item?.textProperty
    }
    val completed = bind(autocommit = true) { item?.completedProperty }
}

enum class FilterState { All, Active, Completed }