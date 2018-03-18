#!/usr/bin/python
# -*- coding: utf-8 -*-

import requests
import urllib
from bs4 import BeautifulSoup
import json
import sys
import logging


class AccountUnlock:
        def __init__(self,config):
                self.url=config["main"]["url"]
                self.netid=config["user"]["netid"]
                self.domain=config["user"]["domain"]
                self.answer1=config["questions"]["answer1"]
                self.answer2=config["questions"]["answer2"]
                self.answer3=config["questions"]["answer3"]
                self.s=requests.Session()
                 

        def __get_first_page(self):
                r=self.s.get(self.url)
                pars = BeautifulSoup(r.text,"html.parser")
                logging.debug("Got 1st page:"+r.text)
                try:
                        self.viewState=pars.find('input',{'id': '__VIEWSTATE'}).get('value')
                        self.eventValidation=pars.find('input',{'id': '__EVENTVALIDATION'}).get('value')
                except AttributeError:
                        logging.error("__get_first_page: Incorrect response")
                        logging.debug("Response:"+r.text)
                        sys.exit(1)
                self.html=r.text
                self.status=r.status_code
                self.cookies=r.cookies

        def __send_reply(self,data,headers={"Accept-Encoding": "gzip, deflate, br"}):
                data["__VIEWSTATE"]=self.viewState
                data["__EVENTVALIDATION"]=self.eventValidation
                data["__ASYNCPOST"]="false"
                logging.debug("Sending data:"+json.dumps(data,indent=4))
                              
              #  Debug output of request    
              #  r=requests.Request('POST',self.url,data=urllib.urlencode(data),headers=headers)
              #  req = r.prepare()
              #  print('{}\n{}\n{}\n\n{}'.format(
              #       '-----------START-----------',
              #       req.method + ' ' + req.url,
              #       '\n'.join('{}: {}'.format(k, v) for k, v in req.headers.items()),
              #       req.body,
              #   ))
                r=self.s.post(self.url,data=data,headers=headers)

                self.html=r.text
                self.status=r.status_code
                pars = BeautifulSoup(r.text,"html.parser")
                try:   
                        self.viewState=pars.find('input',{'id': '__VIEWSTATE'}).get('value')
                        self.eventValidation=pars.find('input',{'id': '__EVENTVALIDATION'}).get('value')
                except AttributeError:
                        logging.error("__send_reply: Incorrect response. Response was:"+r.text)
                        sys.exit(1)

        def __send_user_domain(self):
                data={}
                data["ctl00$ContentPlaceHolder1$ResetWizard$UserNameTextBox"]= self.netid;
                data["ctl00$ContentPlaceHolder1$ResetWizard$LogonDomainDropList"]=self.domain;
                data["ctl00$ContentPlaceHolder1$ResetWizard$StartNavigationTemplateContainerID$StartNextButton"]="Dalej";
                headers={"Accept-Encoding": "gzip, deflate, br"}
                self.__send_reply(data,headers)

        def __send_answer(self,myAnswer):
                data={}
                data["ctl00$ContentPlaceHolder1$ResetWizard$QuestionAnswerTextBox"]=myAnswer
                data["ctl00$ScriptManager1"]="ctl00$UpdatePanel1|ctl00$ContentPlaceHolder1$ResetWizard$StepNavigationTemplateContainerID$StepNextButton"
                data["ctl00$ContentPlaceHolder1$ResetWizard$StepNavigationTemplateContainerID$StepNextButton"]="Dalej"
                headers={"Accept-Encoding": "gzip, deflate"}
                self.__send_reply(data,headers=headers)
        
        def __unLockIfLocked(self):
                pars= BeautifulSoup(self.html,"html.parser")
                try:
                        unlock=pars.find('input',{'name': 'ctl00$ContentPlaceHolder1$ResetWizard$UnlockMethodList'}).get('value')
                        if unlock=="1":
                                logging.info("Account was locked - sending unlock requests") 
                                data={}
                                data["ctl00$ContentPlaceHolder1$ResetWizard$StepNavigationTemplateContainerID$StepNextButton"]="Dalej"
                                data["ctl00$ContentPlaceHolder1$ResetWizard$UnlockMethodList"]="2"
                                data["ctl00$ScriptManager1"]="ctl00$UpdatePanel1|ctl00$ContentPlaceHolder1$ResetWizard$StepNavigationTemplateContainerID$StepNextButton"
                                self.__send_reply(data)
                                data={}
                                data["ctl00$ContentPlaceHolder1$ResetWizard$FinishNavigationTemplateContainerID$FinishButton"]="Zakończ"
                                data["ctl00$ScriptManager1"]="ctl00$UpdatePanel1|ctl00$ContentPlaceHolder1$ResetWizard$FinishNavigationTemplateContainerID$FinishButton"
                                self.__send_reply(data)
                               
                except  AttributeError:
                        logging.info("Not locked")
                

        def run(self):
                self.__get_first_page()
                self.__send_user_domain()
                self.__send_answer(self.answer1)
                self.__send_answer(self.answer2)
                self.__send_answer(self.answer3)
                self.__unLockIfLocked()

        def dumpMyState(self):
                print("ViewState:"+self.viewState)
                print("EventValidation:"+self.eventValidation)
                print("HTML:"+self.html)
                print("Status:"+str(self.status))


if __name__=="__main__":
        import configparser
        config=configparser.ConfigParser()
        config.read('./etc/specops-unlock.conf')

	logging.basicConfig(name="Specops-account-unlock",filename=config['main']['logfile'], level=getattr(logging,config['main']['loglevel'].upper()),format='%(asctime)s - %(filename)s - %(process)d - %(levelname)s - %(message)s')


        unlocker=AccountUnlock(config)
        unlocker.run()
  #      unlocker.dumpMyState()
