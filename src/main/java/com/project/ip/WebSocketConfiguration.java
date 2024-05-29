package com.project.ip;

import com.project.ip.constants.Constants;
import com.project.ip.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer
{
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config)
    {
        config.enableSimpleBroker(Constants.Prefixes.TOPIC);
        config.setApplicationDestinationPrefixes(Constants.Prefixes.CHAT);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry)
    {
        registry.addEndpoint(Constants.EndPoints.SEND_MESSAGE)
                .addInterceptors(new AddHttpSessionInterceptor());
    }

    public class AddHttpSessionInterceptor implements HandshakeInterceptor
    {
        @Override
        public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception
        {
            if (request instanceof ServletServerHttpRequest)
            {
                ServletServerHttpRequest servletRequest = (ServletServerHttpRequest)request;
                HttpSession session = servletRequest.getServletRequest().getSession();
                User user = (User)session.getAttribute(Constants.Attributes.LOGGED_IN_USER);
                attributes.put(Constants.Attributes.LOGGED_IN_USER_ID, user.getId());
            }
            return true;
        }

        @Override
        public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception)
        {}
    }
}
