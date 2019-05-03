# -*- coding: utf-8 -*-
from bs4 import BeautifulSoup
import requests
import unicodedata
import sys
import os

"""script to get the height of famous people by crawling on google, made by me: Bruno Henrique
for an application test of pitang internship"""

def find_height(content):
	soup = BeautifulSoup(content, "lxml")
	isNext = False
	for tag in soup.find_all("span", text=True):

		if not isNext:
			if str.__contains__(tag.text.encode('utf-8'), "Altura"):
				isNext=True

		elif isNext:
		
			return tag.text.encode('utf-8')
	  

try:
    name = ""
    
    for param in sys.argv[1:]:
    	name = name+param+"+"
    
    page = requests.get("https://www.google.com/search?q="+name)
    content = find_height(page.text)
    
    if not content:
        content = "None"
    arq = open(os.path.dirname(os.path.realpath("find-height-script.py"))+"/answer.txt", 'w')
    arq.write(content)
    arq.close
except Exception:
    print("some error ocurred!")


