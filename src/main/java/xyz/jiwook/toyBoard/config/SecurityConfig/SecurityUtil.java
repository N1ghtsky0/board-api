package xyz.jiwook.toyBoard.config.SecurityConfig;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import xyz.jiwook.toyBoard.entity.AdminEntity;
import xyz.jiwook.toyBoard.entity.BaseAccountEntity;
import xyz.jiwook.toyBoard.entity.BaseEntity;

@Service
public class SecurityUtil {
    public String getCurrentUsername() {
        BaseAccountEntity currentUser = this.getCurrentUser();
        if (currentUser == null) {
            return "anonymous";
        }
        return currentUser.getUsername();
    }

    public BaseAccountEntity getCurrentUser() {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser == null || currentUser instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return (BaseAccountEntity) currentUser.getPrincipal();
    }

    public boolean hasAuthority(BaseEntity targetEntity) {
        boolean isAdmin = getCurrentUser() instanceof AdminEntity;
        boolean isOwner;
        if (targetEntity instanceof BaseAccountEntity) {
            isOwner = getCurrentUsername().equals(((BaseAccountEntity) targetEntity).getUsername());
        } else {
            isOwner = getCurrentUsername().equals(targetEntity.getCreatedBy());
        }
        return isAdmin || isOwner;
    }
}
