import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.config.annotation.CorsRegistry

@Configuration
class WebConfig : WebMvcConfigurer {
  override fun addCorsMappings(registry: CorsRegistry) {
    registry
      .addMapping("/**")                              
      .allowedOriginPatterns("http://localhost:8080", "https://news-scraper.pages.dev")
      .allowedMethods("GET", "POST", "PUT", "DELETE") 
      .allowedHeaders("*")                            
      .allowCredentials(true)                         
      .maxAge(3600)                                   
  }
}