package com.leftjoiners.bancosol.proyectobackend.controller;

import com.leftjoiners.bancosol.proyectobackend.dto.Usuario;
import com.leftjoiners.bancosol.proyectobackend.entity.UsuarioEntity;
import com.leftjoiners.bancosol.proyectobackend.dao.UsuarioRepository;
import com.leftjoiners.bancosol.proyectobackend.security.JwtUtil;
import com.leftjoiners.bancosol.proyectobackend.service.UsuarioService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@AllArgsConstructor
public class AuthController {

    // Las dependencias se marcan como final
    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;

    @GetMapping("/")
    public String showLogin() {
        return "auth";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam("username") String username,
                          @RequestParam("password") String password,
                          HttpServletResponse response,
                          HttpSession session,
                          Model model) {
        Usuario usuario = usuarioService.autenticar(username, password);

        if (usuario != null) {
            String token = jwtUtil.generateToken(username);

            Cookie cookie = new Cookie("jwtToken", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 10);
            response.addCookie(cookie);

            session.setAttribute("user", usuario);

            return "redirect:/campanyas";
        } else {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
            return "auth";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response, HttpSession session) {
        Cookie cookie = new Cookie("jwtToken", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        session.invalidate();

        return "redirect:/";
    }
}