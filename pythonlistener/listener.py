import socket
import json

ROBOT_LISTENER_API_VERSION = 2

def start_suite(name, attr):
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.connect(("localhost",9999))
        attr["event"] = "start_suite"
        s.sendall(json.dumps(attr).encode("utf-8"));

def end_suite(name, attr):
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.connect(("localhost",9999))
        attr["event"] = "end_suite"
        s.sendall(json.dumps(attr).encode("utf-8"));

def start_test(name, attr):
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.connect(("localhost",9999))
        attr["event"] = "start_test"
        s.sendall(json.dumps(attr).encode("utf-8"));

def end_test(name, attr):
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.connect(("localhost",9999))
        attr["event"] = "end_test"
        s.sendall(json.dumps(attr).encode("utf-8"));

def start_keyword(name, attr):
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.connect(("localhost",9999))
        attr["event"] = "start_keyword"
        s.sendall(json.dumps(attr).encode("utf-8"));

def end_keyword(name, attr):
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.connect(("localhost",9999))
        attr["event"] = "end_keyword"
        s.sendall(json.dumps(attr).encode("utf-8"));

def log_message(message):
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.connect(("localhost",9999))
        attr = {}
        attr["event"] = "log_message"
        attr["message"] = message
        s.sendall(json.dumps(attr).encode("utf-8"));

def message(message):
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.connect(("localhost",9999))
        attr = {}
        attr["event"] = "message"
        attr["message"] = message
        s.sendall(json.dumps(attr).encode("utf-8"));

def library_import(name, attr):
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.connect(("localhost",9999))
        attr = {}
        attr["event"] = "library_import"
        s.sendall(json.dumps(attr).encode("utf-8"));

def resource_import(name, attr):
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.connect(("localhost",9999))
        attr["event"] = "resource_import"
        s.sendall(json.dumps(attr).encode("utf-8"));

def variables_import(name, attr):
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.connect(("localhost",9999))
        attr["event"] = "variables_import"
        s.sendall(json.dumps(attr).encode("utf-8"));

def output_file(path):
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.connect(("localhost",9999))
        attr = {}
        attr["event"] = "output_file"
        attr["path"] = path
        s.sendall(json.dumps(attr).encode("utf-8"));

def log_file(path):
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.connect(("localhost",9999))
        attr = {}
        attr["event"] = "log_file"
        attr["path"] = path
        s.sendall(json.dumps(attr).encode("utf-8"));

def report_file(path):
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.connect(("localhost",9999))
        attr = {}
        attr["event"] = "report_file"
        attr["path"] = path
        s.sendall(json.dumps(attr).encode("utf-8"));

def xunit_file(path):
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.connect(("localhost",9999))
        attr = {}
        attr["event"] = "xunit_file"
        attr["path"] = path
        s.sendall(json.dumps(attr).encode("utf-8"));


def debug_file(path):
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.connect(("localhost",9999))
        attr = {}
        attr["event"] = "debug_file"
        attr["path"] = path
        s.sendall(json.dumps(attr).encode("utf-8"));

def close():
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.connect(("localhost",9999))
        attr = {}
        attr["event"] = "close"
        s.sendall(json.dumps(attr).encode("utf-8"));