package com.github.gotz9.demo.batch.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class BatchJobsConfiguration {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    public BatchJobsConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Configuration
    class Example1JobConfiguration {

        Logger logger = LoggerFactory.getLogger("batch.examples.batch-example-1");

        ItemReader<Long> reader() {
            List<Long> items = new Random()
                    .longs(10)
                    .boxed()
                    .collect(Collectors.toList());

            logger.debug("generated items for reader: {}", items);

            return new ListItemReader<>(items);
        }

        Step step() {
            return stepBuilderFactory.get("example-1-step-1")
                    .<Long, Void>chunk(2)
                    .reader(reader())
                    .processor((Function<? super Long, ? extends Void>) item -> {
                        logger.debug("processing item {}", item);
                        return null;
                    })
                    .writer(items -> {
                    })
                    .build();
        }

        @Bean("job-example-1")
        Job job() {
            return jobBuilderFactory.get("job-example-1")
                    .start(step())
                    .build();
        }

    }

}

