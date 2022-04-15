# SpringSecuirty
This is a Spring Security practice repository. It is based on this youtube video https://www.youtube.com/watch?v=her_7pa0vrg&amp;list=WL&amp;index=98&amp;t=1040s

## Changing github credentials
To set GitHub credentials for first time run
``git config credential.helper store``
- You can configure global credentials using ``git config --global credential.helper store``
- You can ensure that you are configuring local credentials using ``git config --local credential.helper store``
To change GitHub credentials we will need first to unset the credential helper.
To do it, run this command 
``git config --global --unset credential.helper`` 
You can choose between global and local changing ``--global`` to ``--local``
