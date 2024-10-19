package br.com.fiap.tech.sub_tech_challenge_management_api_fase4.entrypoint.api.controller;

import br.com.fiap.tech.sub_tech_challenge_management_api_fase4.adapter.entrypoint.api.controller.VehicleController;
import br.com.fiap.tech.sub_tech_challenge_management_api_fase4.application.vehicle.entities.VehicleEntity;
import br.com.fiap.tech.sub_tech_challenge_management_api_fase4.application.vehicle.services.VehicleService;
import br.com.fiap.tech.sub_tech_challenge_management_api_fase4.infrastructure.handler.GlobalExceptionHandler;
import br.com.fiap.tech.sub_tech_challenge_management_api_fase4.utils.TestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class VehicleControllerTest {

    private MockMvc mockMvc;
    AutoCloseable mock;

    @Mock
    private VehicleService vehicleService;


    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        VehicleController vehicleController = new VehicleController(vehicleService);

        mockMvc = MockMvcBuilders.standaloneSetup(vehicleController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .addFilter((request, response, chain) -> {
                    response.setCharacterEncoding("UTF-8");
                    chain.doFilter(request, response);
                }, "/*")
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Nested
    class vehicleControllerTest {

        @Test
        void findById() throws Exception {

            VehicleEntity vehicleEntity = TestUtils.buildVehicleEntity();
            when(vehicleService.findById(vehicleEntity.getId())).thenReturn(vehicleEntity);

            mockMvc.perform(get("/api/vehicle/{id}", vehicleEntity.getId()))
                    .andDo(print())
                    .andExpect(status().isOk());

            verify(vehicleService, times(1)).findById(vehicleEntity.getId());

        }

        @Test
        void shouldFindAll() throws Exception {

            List<VehicleEntity> vehicles = new ArrayList<>();
            vehicles.add(TestUtils.anotherBuildVehicleEntity());
            vehicles.add(TestUtils.buildVehicleEntity());
            vehicles.add(TestUtils.buildVehicleEntity());

            when(vehicleService.listVehicles()).thenReturn(vehicles);

            mockMvc.perform(get("/api/vehicle"))
                    .andDo(print())
                    .andExpect(status().isOk());

            verify(vehicleService, times(1)).listVehicles();

        }

        @Test
        void save() throws Exception {
            VehicleEntity vehicleEntity = TestUtils.buildVehicleEntityWithoutId();

            when(vehicleService.registerVehicle(any(VehicleEntity.class)))
                    .thenAnswer(i -> i.getArgument(0));

            mockMvc.perform(
                    post("/api/vehicle")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(vehicleEntity))
            )
                    .andDo(print())
                    .andExpect(status().isCreated());


            verify(vehicleService, times(1)).registerVehicle(any(VehicleEntity.class));
        }

        @Test
        void update() throws Exception{

            VehicleEntity vehicleEntity = TestUtils.buildVehicleEntity();

            when(vehicleService.updateVehicleRegister(any(VehicleEntity.class))).thenAnswer(i -> i.getArgument(0));

            mockMvc.perform(put("/api/vehicle")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(vehicleEntity))
            ).andExpect(status().isOk())
                    .andDo(print());

            verify(vehicleService, times(1)).updateVehicleRegister(any(VehicleEntity.class));
        }

    }


    public static String asJsonString(final Object obj) throws JsonProcessingException {

        ObjectMapper om = new ObjectMapper();

        om.registerModule(new JavaTimeModule());

        return om.writeValueAsString(obj);
    }


}