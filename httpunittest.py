#!/usr/bin/env python
#-*- coding: utf-8 -*-
import sys
import codecs
import json
import unittest

from com.cmdLib import *
from com.logLib import *
from com.parsetestcaseLib import *

filepath="./case/items.test.case"
testcase=get_testcase(filepath)
ip="127.0.0.1"
port="18999"
method="POST"
url="/lbs/da/openservice"

class httpunittest(unittest.TestCase):
    def service_proc(self,test_case_id):
        logging.debug("***********************************************************************************************")
        logging.debug("test_case_id : " + test_case_id)
        case_name=testcase[test_case_id]['info']['name']
        case_input=testcase[test_case_id]['input']
        case_expect=testcase[test_case_id]['expect']

        fp=codecs.open("./data/tmp.data","w","utf-8")
        fp.write(json.dumps(case_input))
        fp.close()
        cmdstr="java -jar httpclient.jar " + ip + " " + port + " " + "./data/tmp.data"
        logging.debug("cmdstr : " + cmdstr)
        status,output=cmd_execute(cmdstr)
        output=json.loads(output)
        logging.debug("expect : " + json.dumps(case_expect))
        logging.debug("output : " + json.dumps(output))
        assert(output == case_expect)
        logging.debug("***********************************************************************************************\n\n")

    for test_case_id in testcase:
        exec("def test_%s(self): self.service_proc('%s')" %(test_case_id,test_case_id))

if __name__ == '__main__':
    TestSuit=unittest.TestLoader().loadTestsFromTestCase(httpunittest)
    unittest.TextTestRunner(verbosity=2).run(TestSuit)


