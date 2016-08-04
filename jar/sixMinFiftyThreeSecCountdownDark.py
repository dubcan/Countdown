#!/usr/bin/env python

import subprocess
subprocess.call(['java', '-jar', '-Xmx20M', 'countdown.jar', '413000', 'dark'])