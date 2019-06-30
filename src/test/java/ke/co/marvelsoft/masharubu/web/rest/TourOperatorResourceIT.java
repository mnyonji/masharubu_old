package ke.co.marvelsoft.masharubu.web.rest;

import ke.co.marvelsoft.masharubu.MasharubuApp;
import ke.co.marvelsoft.masharubu.domain.TourOperator;
import ke.co.marvelsoft.masharubu.repository.TourOperatorRepository;
import ke.co.marvelsoft.masharubu.service.TourOperatorService;
import ke.co.marvelsoft.masharubu.service.dto.TourOperatorDTO;
import ke.co.marvelsoft.masharubu.service.mapper.TourOperatorMapper;
import ke.co.marvelsoft.masharubu.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static ke.co.marvelsoft.masharubu.web.rest.TestUtil.sameInstant;
import static ke.co.marvelsoft.masharubu.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ke.co.marvelsoft.masharubu.domain.enumeration.Status;
/**
 * Integration tests for the {@Link TourOperatorResource} REST controller.
 */
@SpringBootTest(classes = MasharubuApp.class)
public class TourOperatorResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NBR = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NBR = "BBBBBBBBBB";

    private static final String DEFAULT_EMIL_ADDR = "AAAAAAAAAA";
    private static final String UPDATED_EMIL_ADDR = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.NEW;
    private static final Status UPDATED_STATUS = Status.ACTIVE;

    private static final ZonedDateTime DEFAULT_DATE_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_VALIDATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_VALIDATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_PHYSICAL_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_PHYSICAL_ADDRESS = "BBBBBBBBBB";

    @Autowired
    private TourOperatorRepository tourOperatorRepository;

    @Autowired
    private TourOperatorMapper tourOperatorMapper;

    @Autowired
    private TourOperatorService tourOperatorService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restTourOperatorMockMvc;

    private TourOperator tourOperator;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TourOperatorResource tourOperatorResource = new TourOperatorResource(tourOperatorService);
        this.restTourOperatorMockMvc = MockMvcBuilders.standaloneSetup(tourOperatorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TourOperator createEntity(EntityManager em) {
        TourOperator tourOperator = new TourOperator()
            .name(DEFAULT_NAME)
            .phoneNbr(DEFAULT_PHONE_NBR)
            .emilAddr(DEFAULT_EMIL_ADDR)
            .status(DEFAULT_STATUS)
            .dateCreated(DEFAULT_DATE_CREATED)
            .dateValidated(DEFAULT_DATE_VALIDATED)
            .physicalAddress(DEFAULT_PHYSICAL_ADDRESS);
        return tourOperator;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TourOperator createUpdatedEntity(EntityManager em) {
        TourOperator tourOperator = new TourOperator()
            .name(UPDATED_NAME)
            .phoneNbr(UPDATED_PHONE_NBR)
            .emilAddr(UPDATED_EMIL_ADDR)
            .status(UPDATED_STATUS)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateValidated(UPDATED_DATE_VALIDATED)
            .physicalAddress(UPDATED_PHYSICAL_ADDRESS);
        return tourOperator;
    }

    @BeforeEach
    public void initTest() {
        tourOperator = createEntity(em);
    }

    @Test
    @Transactional
    public void createTourOperator() throws Exception {
        int databaseSizeBeforeCreate = tourOperatorRepository.findAll().size();

        // Create the TourOperator
        TourOperatorDTO tourOperatorDTO = tourOperatorMapper.toDto(tourOperator);
        restTourOperatorMockMvc.perform(post("/api/tour-operators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tourOperatorDTO)))
            .andExpect(status().isCreated());

        // Validate the TourOperator in the database
        List<TourOperator> tourOperatorList = tourOperatorRepository.findAll();
        assertThat(tourOperatorList).hasSize(databaseSizeBeforeCreate + 1);
        TourOperator testTourOperator = tourOperatorList.get(tourOperatorList.size() - 1);
        assertThat(testTourOperator.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTourOperator.getPhoneNbr()).isEqualTo(DEFAULT_PHONE_NBR);
        assertThat(testTourOperator.getEmilAddr()).isEqualTo(DEFAULT_EMIL_ADDR);
        assertThat(testTourOperator.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTourOperator.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testTourOperator.getDateValidated()).isEqualTo(DEFAULT_DATE_VALIDATED);
        assertThat(testTourOperator.getPhysicalAddress()).isEqualTo(DEFAULT_PHYSICAL_ADDRESS);
    }

    @Test
    @Transactional
    public void createTourOperatorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tourOperatorRepository.findAll().size();

        // Create the TourOperator with an existing ID
        tourOperator.setId(1L);
        TourOperatorDTO tourOperatorDTO = tourOperatorMapper.toDto(tourOperator);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTourOperatorMockMvc.perform(post("/api/tour-operators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tourOperatorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TourOperator in the database
        List<TourOperator> tourOperatorList = tourOperatorRepository.findAll();
        assertThat(tourOperatorList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = tourOperatorRepository.findAll().size();
        // set the field null
        tourOperator.setName(null);

        // Create the TourOperator, which fails.
        TourOperatorDTO tourOperatorDTO = tourOperatorMapper.toDto(tourOperator);

        restTourOperatorMockMvc.perform(post("/api/tour-operators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tourOperatorDTO)))
            .andExpect(status().isBadRequest());

        List<TourOperator> tourOperatorList = tourOperatorRepository.findAll();
        assertThat(tourOperatorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneNbrIsRequired() throws Exception {
        int databaseSizeBeforeTest = tourOperatorRepository.findAll().size();
        // set the field null
        tourOperator.setPhoneNbr(null);

        // Create the TourOperator, which fails.
        TourOperatorDTO tourOperatorDTO = tourOperatorMapper.toDto(tourOperator);

        restTourOperatorMockMvc.perform(post("/api/tour-operators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tourOperatorDTO)))
            .andExpect(status().isBadRequest());

        List<TourOperator> tourOperatorList = tourOperatorRepository.findAll();
        assertThat(tourOperatorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmilAddrIsRequired() throws Exception {
        int databaseSizeBeforeTest = tourOperatorRepository.findAll().size();
        // set the field null
        tourOperator.setEmilAddr(null);

        // Create the TourOperator, which fails.
        TourOperatorDTO tourOperatorDTO = tourOperatorMapper.toDto(tourOperator);

        restTourOperatorMockMvc.perform(post("/api/tour-operators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tourOperatorDTO)))
            .andExpect(status().isBadRequest());

        List<TourOperator> tourOperatorList = tourOperatorRepository.findAll();
        assertThat(tourOperatorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = tourOperatorRepository.findAll().size();
        // set the field null
        tourOperator.setStatus(null);

        // Create the TourOperator, which fails.
        TourOperatorDTO tourOperatorDTO = tourOperatorMapper.toDto(tourOperator);

        restTourOperatorMockMvc.perform(post("/api/tour-operators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tourOperatorDTO)))
            .andExpect(status().isBadRequest());

        List<TourOperator> tourOperatorList = tourOperatorRepository.findAll();
        assertThat(tourOperatorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhysicalAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = tourOperatorRepository.findAll().size();
        // set the field null
        tourOperator.setPhysicalAddress(null);

        // Create the TourOperator, which fails.
        TourOperatorDTO tourOperatorDTO = tourOperatorMapper.toDto(tourOperator);

        restTourOperatorMockMvc.perform(post("/api/tour-operators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tourOperatorDTO)))
            .andExpect(status().isBadRequest());

        List<TourOperator> tourOperatorList = tourOperatorRepository.findAll();
        assertThat(tourOperatorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTourOperators() throws Exception {
        // Initialize the database
        tourOperatorRepository.saveAndFlush(tourOperator);

        // Get all the tourOperatorList
        restTourOperatorMockMvc.perform(get("/api/tour-operators?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tourOperator.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].phoneNbr").value(hasItem(DEFAULT_PHONE_NBR.toString())))
            .andExpect(jsonPath("$.[*].emilAddr").value(hasItem(DEFAULT_EMIL_ADDR.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(sameInstant(DEFAULT_DATE_CREATED))))
            .andExpect(jsonPath("$.[*].dateValidated").value(hasItem(sameInstant(DEFAULT_DATE_VALIDATED))))
            .andExpect(jsonPath("$.[*].physicalAddress").value(hasItem(DEFAULT_PHYSICAL_ADDRESS.toString())));
    }
    
    @Test
    @Transactional
    public void getTourOperator() throws Exception {
        // Initialize the database
        tourOperatorRepository.saveAndFlush(tourOperator);

        // Get the tourOperator
        restTourOperatorMockMvc.perform(get("/api/tour-operators/{id}", tourOperator.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tourOperator.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.phoneNbr").value(DEFAULT_PHONE_NBR.toString()))
            .andExpect(jsonPath("$.emilAddr").value(DEFAULT_EMIL_ADDR.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.dateCreated").value(sameInstant(DEFAULT_DATE_CREATED)))
            .andExpect(jsonPath("$.dateValidated").value(sameInstant(DEFAULT_DATE_VALIDATED)))
            .andExpect(jsonPath("$.physicalAddress").value(DEFAULT_PHYSICAL_ADDRESS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTourOperator() throws Exception {
        // Get the tourOperator
        restTourOperatorMockMvc.perform(get("/api/tour-operators/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTourOperator() throws Exception {
        // Initialize the database
        tourOperatorRepository.saveAndFlush(tourOperator);

        int databaseSizeBeforeUpdate = tourOperatorRepository.findAll().size();

        // Update the tourOperator
        TourOperator updatedTourOperator = tourOperatorRepository.findById(tourOperator.getId()).get();
        // Disconnect from session so that the updates on updatedTourOperator are not directly saved in db
        em.detach(updatedTourOperator);
        updatedTourOperator
            .name(UPDATED_NAME)
            .phoneNbr(UPDATED_PHONE_NBR)
            .emilAddr(UPDATED_EMIL_ADDR)
            .status(UPDATED_STATUS)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateValidated(UPDATED_DATE_VALIDATED)
            .physicalAddress(UPDATED_PHYSICAL_ADDRESS);
        TourOperatorDTO tourOperatorDTO = tourOperatorMapper.toDto(updatedTourOperator);

        restTourOperatorMockMvc.perform(put("/api/tour-operators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tourOperatorDTO)))
            .andExpect(status().isOk());

        // Validate the TourOperator in the database
        List<TourOperator> tourOperatorList = tourOperatorRepository.findAll();
        assertThat(tourOperatorList).hasSize(databaseSizeBeforeUpdate);
        TourOperator testTourOperator = tourOperatorList.get(tourOperatorList.size() - 1);
        assertThat(testTourOperator.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTourOperator.getPhoneNbr()).isEqualTo(UPDATED_PHONE_NBR);
        assertThat(testTourOperator.getEmilAddr()).isEqualTo(UPDATED_EMIL_ADDR);
        assertThat(testTourOperator.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTourOperator.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testTourOperator.getDateValidated()).isEqualTo(UPDATED_DATE_VALIDATED);
        assertThat(testTourOperator.getPhysicalAddress()).isEqualTo(UPDATED_PHYSICAL_ADDRESS);
    }

    @Test
    @Transactional
    public void updateNonExistingTourOperator() throws Exception {
        int databaseSizeBeforeUpdate = tourOperatorRepository.findAll().size();

        // Create the TourOperator
        TourOperatorDTO tourOperatorDTO = tourOperatorMapper.toDto(tourOperator);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTourOperatorMockMvc.perform(put("/api/tour-operators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tourOperatorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TourOperator in the database
        List<TourOperator> tourOperatorList = tourOperatorRepository.findAll();
        assertThat(tourOperatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTourOperator() throws Exception {
        // Initialize the database
        tourOperatorRepository.saveAndFlush(tourOperator);

        int databaseSizeBeforeDelete = tourOperatorRepository.findAll().size();

        // Delete the tourOperator
        restTourOperatorMockMvc.perform(delete("/api/tour-operators/{id}", tourOperator.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TourOperator> tourOperatorList = tourOperatorRepository.findAll();
        assertThat(tourOperatorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TourOperator.class);
        TourOperator tourOperator1 = new TourOperator();
        tourOperator1.setId(1L);
        TourOperator tourOperator2 = new TourOperator();
        tourOperator2.setId(tourOperator1.getId());
        assertThat(tourOperator1).isEqualTo(tourOperator2);
        tourOperator2.setId(2L);
        assertThat(tourOperator1).isNotEqualTo(tourOperator2);
        tourOperator1.setId(null);
        assertThat(tourOperator1).isNotEqualTo(tourOperator2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TourOperatorDTO.class);
        TourOperatorDTO tourOperatorDTO1 = new TourOperatorDTO();
        tourOperatorDTO1.setId(1L);
        TourOperatorDTO tourOperatorDTO2 = new TourOperatorDTO();
        assertThat(tourOperatorDTO1).isNotEqualTo(tourOperatorDTO2);
        tourOperatorDTO2.setId(tourOperatorDTO1.getId());
        assertThat(tourOperatorDTO1).isEqualTo(tourOperatorDTO2);
        tourOperatorDTO2.setId(2L);
        assertThat(tourOperatorDTO1).isNotEqualTo(tourOperatorDTO2);
        tourOperatorDTO1.setId(null);
        assertThat(tourOperatorDTO1).isNotEqualTo(tourOperatorDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(tourOperatorMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(tourOperatorMapper.fromId(null)).isNull();
    }
}
