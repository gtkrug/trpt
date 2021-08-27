import edu.gatech.gtri.trustmark.trpt.listener.AuthFailureSecurityListener
import edu.gatech.gtri.trustmark.trpt.listener.AuthSuccessSecurityListener
import edu.gatech.gtri.trustmark.trpt.listener.UserPasswordEncoderListener

beans = {
    userPasswordEncoderListener(UserPasswordEncoderListener)
    authFailureListener(AuthFailureSecurityListener.class)
    authSuccessListener(AuthSuccessSecurityListener.class)
}
