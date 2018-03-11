# Specops-password-unlocker

This repository contains a code you can use for automation of account unlock over AD Self Service Password Reset utility. If supported scenario doesn't work in your environment and you'd like to use the script feel free to open the pull or feature request. 

If you want to read more about how it works, check my following blog scripts:
https://funinit.wordpress.com/2017/10/03/asp-net-web-form-as-api/

##How to use the script?

Clone the repository:
```
git clone https://github.com/cinek810/Specops-password-unlocker.git
```
Update configuretion file
```
vim etc/specops-unlock.conf
```
and run the script to unlock your account:
```
python2.7 ./AccountUnlock.py
```
##Changelog

First working version - supports only unlock scenario where 1st page is a form for netid and domain selection and then three security are asked. 
