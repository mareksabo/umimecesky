package cz.muni.fi.umimecesky.pojo

import java.io.Serializable

class Category(val id: Int, val name: String) : Serializable {

    override fun toString(): String {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}'
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val category = other as Category?

        return id == category!!.id && name == category.name

    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        return result
    }

}
