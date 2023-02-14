package es.josetesan.springbatchxmlparser.data;

import com.thoughtworks.xstream.security.AnyTypePermission;
import java.util.Map;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class JobConfiguration {

  private static final Logger LOGGER = LoggerFactory.getLogger(JobConfiguration.class);

  @Bean
  public Job job(
      JobRepository jobRepository, JobExecutionListener jobExecutionListener, Step step1) {
    return new JobBuilder("job", jobRepository).listener(jobExecutionListener).start(step1).build();
  }

  @Bean
  public Step step1(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      DataSource dataSource) {

    return new StepBuilder("step", jobRepository)
        .<Laboratorio, Laboratorio>chunk(10, transactionManager)
        .reader(reader())
        //            .processor(item -> {
        //                LOGGER.info("Laboratios is {}",item);
        //                return item;
        //            })
        .writer(writer(dataSource))
        .build();
  }

  @Bean
  public JdbcBatchItemWriter<Laboratorio> writer(DataSource dataSource) {
    JdbcBatchItemWriter<Laboratorio> writer = new JdbcBatchItemWriter<>();
    writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
    writer.setSql(
        """
                insert into Laboratorio(codigolaboratorio,laboratorio,direccion, codigopostal,localidad,cif)
                values
                 (:codigolaboratorio,:laboratorio,:direccion,:codigopostal,:localidad,:cif)
                """);
    writer.setDataSource(dataSource);
    return writer;
  }

  @Bean
  public StaxEventItemReader<Laboratorio> reader() {

    return new StaxEventItemReaderBuilder<Laboratorio>()
        .name("laboratorioItemReader")
        .resource(new ClassPathResource("laboratorios.xml"))
        .addFragmentRootElements("laboratorios")
        .strict(false)
        .unmarshaller(laboratorioMarshaller())
        .build();
  }

  @Bean
  public XStreamMarshaller laboratorioMarshaller() {
    Map<String, Class> aliases =
        Map.of(
            "laboratorios", Laboratorio.class,
            "codigolaboratorio", Integer.class,
            "laboratorio", String.class,
            "direccion", String.class,
            "codigopostal", String.class,
            "localidad", String.class);
    XStreamMarshaller unmarshaller = new XStreamMarshaller();
    unmarshaller.setSupportedClasses(Laboratorio.class);
    unmarshaller.setTypePermissions(new AnyTypePermission());
    unmarshaller.setAliases(aliases);
    return unmarshaller;
  }
}
