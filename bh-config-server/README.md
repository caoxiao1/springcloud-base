
统一配置中心

对于每一个服务中可变更的配置，将会使用 svn、gitlab 对配置文件进行版本管理，通过在不同环境下激活不同的配置文件，然后从配置文件服务器上拉取对应的配置文件
最后通过 spring boot actuator 的 /refresh endpoint 来通知服务更新配置
