package dk.sdu.cbse.scoreservice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

// Verificerer REST-kontrakten med MockMvc (uden rigtig server-socket).
@WebMvcTest(ScoreController.class)
class ScoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Controlleren deler state i den genbrugte context, så vi nulstiller
    // pr. test for at gøre metoderne uafhængige af hinandens rækkefølge.
    @BeforeEach
    void resetScore() throws Exception {
        mockMvc.perform(post("/score/reset"));
    }

    @Test
    void initialScoreIsZero() throws Exception {
        mockMvc.perform(get("/score"))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));
    }

    @Test
    void addPointIncrementsScore() throws Exception {
        mockMvc.perform(post("/score/add"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
        mockMvc.perform(post("/score/add"))
                .andExpect(content().string("2"));
    }

    @Test
    void deductIsFlooredAtZero() throws Exception {
        mockMvc.perform(post("/score/deduct"))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));
    }
}
