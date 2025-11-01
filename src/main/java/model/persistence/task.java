package model.persistence;

import javax.json.bind.annotation.JsonbProperty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class task {
    static final String STRING_FORMAT = "Task [id=%d, name=%s, description=%s]";
    @JsonProperty("name")
    private String name;
    @JsonProperty("id")
    private int id;
    @JsonProperty("description")
    private String description;

    /**
     *
     * @param name name of the task
     * @param id name of the id
     * @param description name of the description
     *                    {@literal @} JsonProperty is used in serialization and deserialization
     *                    of the JSON object to the Java object in mapping the fields
     */
    public task(@JsonProperty("name") String name,
                @JsonProperty("id") int id,
                @JsonProperty("description") String description) {
        this.name = name;
        this.id = id;
        this.description = description;

    }

    /**
     *
     * @return the name of the task
     */
    public String getName(){
        return this.name;
    }

    /**
     *
     * @return the description of the task
     */
    public String getDescription(){
        return this.description;
    }

    /**
     *
     * @return the id
     */
    public int getId(){
        return this.id;
    }

    /**
     *
     * @return toString format
     */
    @Override
    public String toString(){
        return String.format(STRING_FORMAT, id, name, description);
    }

}

