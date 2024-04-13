package com.devbackend.web_telephone_ttcn;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WebTelephoneTtcnApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebTelephoneTtcnApplication.class, args);
    }

    @Bean
    public Cloudinary cloudinary(){
        Cloudinary c = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dc2o2cyh1",
                "api_key", "255577216694877",
                "api_secret", "vXKuEHtN2TZ6zzByVJvAoDnFhIE",
                "secure",true
        ));
        return c;
    }

}
