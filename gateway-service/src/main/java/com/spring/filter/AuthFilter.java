/**
 * 这里还没有完全掌握
 */

package com.spring.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.pojo.Result;
import com.spring.properties.AuthWhiteName;
import com.spring.utils.JWTUtils;
import io.jsonwebtoken.Claims;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private ObjectMapper objectMapper;

    //配置白名单
    @Autowired
    private AuthWhiteName authWhiteName;

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //拿到request的uri
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        //若url为白名单，则放行
        if(match(path, authWhiteName.getUrl())){
            return chain.filter(exchange);
        }

        //从header中获取token
        String userToken = request.getHeaders().getFirst("user_token");
        if(userToken==null){
            //todo
            return unauthorizedResponse(exchange, "令牌为空");
        }
        Claims claims = JWTUtils.parseJWT(userToken);
        if(claims==null){
            //todo
            return unauthorizedResponse(exchange, "令牌校验失败");
        }
        return chain.filter(exchange);
    }

    /**
     * 判断url是否在白名单中
     * @param path
     * @param url
     * @return
     */
    private boolean match(String path, List<String> url) {
        if(url==null || url.size()==0){
            return false;
        }
        return url.contains(path);
    }

    /**
     * 若认证失败，则返回未授权的响应
     * @param exchange
     * @param message
     * @return
     */
    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String message) throws JsonProcessingException {
        log.error("用户认证失败");
        ServerHttpResponse response=exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        Result result = Result.fail(message);
        DataBuffer dataBuffer = response.bufferFactory().wrap(objectMapper.writeValueAsBytes( result));
        return response.writeWith(Mono.just(dataBuffer));
    }


    /**
     * 这个类是一个完整的过滤器，还有很多个平行的类作为下游过滤器
     * 设置order是为了确定这个过滤器的执行顺序
     */
    public int getOrder() {
        return -200;
    }
}
