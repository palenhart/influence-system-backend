//package com.thecorporateer.influence.security;
//
//import java.util.Collection;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.userdetails.UserDetails;
//
//public class AuthenticatedUser extends User implements UserDetails {
//	
//	private static final long serialVersionUID = 2409890613866773958L;
//
//	protected AuthenticatedUser(User user) {
//        super(user.getUsername(), user.getPassword());
//    }
// 
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return AuthorityUtils.createAuthorityList("ROLE_USER");
//    }
// 
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
// 
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
// 
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
// 
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}