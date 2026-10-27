#!/bin/bash
iptables -S
iptables -F
iptables -P INPUT DROP
iptables -P FORWARD DROP
iptables -P OUTPUT ACCEPT
iptables -A INPUT -i lo -j ACCEPT
iptables -A INPUT -m state --state ESTABLISHED,RELATED -j ACCEPT
iptables -A IN_public_allow -p tcp -m tcp --dport 6347 -m conntrack --ctstate NEW -j ACCEPT
iptables -ESTABLISHED