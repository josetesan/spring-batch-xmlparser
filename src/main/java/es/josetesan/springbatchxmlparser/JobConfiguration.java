package es.josetesan.springbatchxmlparser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableBatchProcessing(dataSourceRef = "batchDataSource", transactionManagerRef = "batchTransactionManager")
public class JobConfiguration {


    private static final Logger LOGGER = LoggerFactory.getLogger(JobConfiguration.class);
    @Bean
    public DataSource batchDataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.HSQL)
            .addScript("/org/springframework/batch/core/schema-hsqldb.sql")
            .generateUniqueName(true).build();
    }

    @Bean
    public JdbcTransactionManager batchTransactionManager(DataSource dataSource) {
        return new JdbcTransactionManager(dataSource);
    }

    @Bean
    public Job job(JobRepository jobRepository,Step step) {
        return new JobBuilder("myJob", jobRepository)
            .incrementer(new RunIdIncrementer())
            .flow(step)
            .end()
            .build();
    }

    @Bean
    public Step step(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager) {
        return new StepBuilder("step", jobRepository)
            .<Laboratorio, Laboratorio> chunk(10, transactionManager)
            .reader(reader())
            .processor(processor())
            .writer(writer())
            .build();
    }

    private ItemWriter<Laboratorio> writer() {
        return chunk -> System.out.println(chunk.size());
    }

    public ItemProcessor<Laboratorio, Laboratorio> processor() {
        return item -> {
            LOGGER.info("Found {}", item);
            return item;
        };
    }


        @Bean
    public StaxEventItemReader<Laboratorio> reader() {
        return new StaxEventItemReaderBuilder<Laboratorio>()
            .name("laboratorioItemReader")
            .resource(new ClassPathResource("laboratorios.xml"))
            .addFragmentRootElements("laboratorios")
            .unmarshaller(laboratorioMarshaller())
            .build();

    }

    @Bean
    public XStreamMarshaller laboratorioMarshaller() {
        Map<String, Class> aliases = Map.of(
            "codigolaboratorio",Integer.class,
            "laboratorio",String.class,
            "direccion",String.class,
            "codigopostal",String.class,
            "localidad",String.class
        );
        XStreamMarshaller unmarshaller = new XStreamMarshaller();
        unmarshaller.setAliases(aliases);
        return unmarshaller;
    }
}
