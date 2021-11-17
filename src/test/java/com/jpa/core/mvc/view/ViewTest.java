package com.jpa.core.mvc.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.jpa.core.mvc.model.Model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ViewTest {

    private String name;
    private View view;

    @BeforeEach
    void setup() {
        name = "test";
        view = new View(name);
    }

    @Test
    void testGetModel() {
        assertNotNull(view.getModel());
    }

    @Test
    void testGetPath() {
        String titleName = name.substring(0, 1).toUpperCase().concat(name.substring(1));
        assertEquals(titleName.concat(View.getFileExtension()), view.getPath());
    }

    @Test
    void testGetViewName() {
        assertEquals(name.substring(0, 1).toUpperCase().concat(name.substring(1)),
                view.getViewName());
    }

    @Test
    void testSetModel() {
        Model model = new Model();
        assertEquals(model, view.setModel(model).getModel());
    }

    @Test
    void testSetPath() {
        String newPath = "newPath.html.hbs";
        assertEquals(newPath, view.setPath(newPath).getPath());
    }

    @Test
    void testSetViewName() {
        String newName = "NewName";
        assertEquals(newName, view.setViewName(newName).getViewName());
    }
}
