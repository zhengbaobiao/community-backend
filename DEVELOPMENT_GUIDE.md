# 社区服务后台管理系统 - 后续开发指南

## 当前完成状态

✅ **已完成模块** (100%):
- 项目初始化与基础架构
- 认证授权模块(含操作日志AOP)

⏳ **待开发模块**:
- 用户管理模块
- 订单管理模块  
- 系统管理模块
- 前端项目开发
- 测试与优化
- 部署上线

---

## 后续开发路线图

### 阶段一: 系统管理模块 (优先级: 高)

#### 为什么先开发系统管理?
因为系统管理模块是后台管理系统的核心,包含:
- 管理员管理(已有认证,需要CRUD)
- 角色管理(权限分配)
- 权限管理(动态菜单)
- 操作日志查询

#### 开发步骤:

**1. 管理员管理模块**
```java
// 需要实现的接口:
POST   /admin/user/list      // 管理员列表(分页、搜索)
GET    /admin/user/{id}      // 管理员详情
POST   /admin/user           // 新增管理员
PUT    /admin/user/{id}      // 更新管理员
DELETE /admin/user/{id}      // 删除管理员
PUT    /admin/user/{id}/password  // 重置密码
PUT    /admin/user/{id}/status    // 启用/禁用
POST   /admin/user/{id}/roles     // 分配角色
```

**2. 角色管理模块**
```java
// 需要实现的接口:
GET    /admin/role/list      // 角色列表
GET    /admin/role/{id}      // 角色详情
POST   /admin/role           // 新增角色
PUT    /admin/role/{id}      // 更新角色
DELETE /admin/role/{id}      // 删除角色
POST   /admin/role/{id}/permissions  // 分配权限
```

**3. 权限管理模块**
```java
// 需要实现的接口:
GET    /admin/permission/tree      // 权限树(菜单结构)
GET    /admin/permission/list      // 权限列表
POST   /admin/permission           // 新增权限
PUT    /admin/permission/{id}      // 更新权限
DELETE /admin/permission/{id}      // 删除权限
```

**4. 操作日志查询**
```java
// 需要实现的接口:
GET    /admin/log/list       // 日志列表(分页、搜索)
GET    /admin/log/{id}       // 日志详情
GET    /admin/log/export     // 导出日志
```

#### 代码示例:

**AdminUserController.java**
```java
@RestController
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class AdminUserController {
    
    private final AdminUserService adminUserService;
    
    @OperationLog("查询管理员列表")
    @GetMapping("/list")
    public Result<PageResult<AdminUserVO>> list(@Validated AdminUserQuery query) {
        return Result.success(adminUserService.list(query));
    }
    
    @OperationLog("新增管理员")
    @PostMapping
    public Result<Void> add(@Validated @RequestBody AdminUserDTO dto) {
        adminUserService.add(dto);
        return Result.success("新增成功");
    }
    
    // ... 其他方法
}
```

### 阶段二: 用户管理模块 (优先级: 中)

这个模块管理的是**社区平台的普通用户**(不是管理员)。

#### 需要准备:
1. 创建用户相关表(如果设计文档中有)
2. 实现用户CRUD
3. 用户状态管理
4. 用户标签管理

#### 代码框架:
```java
// Entity: User.java
// Mapper: UserMapper.java + UserMapper.xml
// Service: UserService.java + UserServiceImpl.java
// Controller: UserController.java
// DTO: UserQuery.java, UserVO.java, UserDTO.java
```

### 阶段三: 订单管理模块 (优先级: 中)

#### 需要准备:
1. 创建订单相关表
2. 订单状态流转逻辑
3. 订单查询和导出

### 阶段四: 前端项目开发 (优先级: 高)

#### 技术栈:
- Vue 3 + TypeScript
- Element Plus
- Vite

#### 项目初始化:
```bash
npm create vite@latest community-admin-frontend -- --template vue-ts
cd community-admin-frontend
npm install element-plus
npm install axios pinia vue-router
npm install @element-plus/icons-vue
```

#### 目录结构:
```
frontend/
├── src/
│   ├── api/           # API接口
│   ├── assets/        # 静态资源
│   ├── components/    # 组件
│   ├── layouts/       # 布局
│   ├── router/        # 路由
│   ├── stores/        # 状态管理
│   ├── utils/         # 工具
│   ├── views/         # 页面
│   ├── App.vue
│   └── main.ts
```

---

## 开发规范

### 1. 命名规范

**Java类命名:**
- Entity: `AdminUser`, `AdminRole`
- DTO: `AdminUserDTO`, `AdminUserVO`, `AdminUserQuery`
- Service: `AdminUserService`, `AdminUserServiceImpl`
- Controller: `AdminUserController`
- Mapper: `AdminUserMapper`

**数据库表命名:**
- 全小写+下划线: `admin_user`, `admin_role`

**接口路径:**
- RESTful风格: `/admin/user`, `/admin/role`

### 2. 代码模板

**Service层模板:**
```java
@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {
    
    private final AdminUserMapper adminUserMapper;
    
    @Override
    public PageResult<AdminUserVO> list(AdminUserQuery query) {
        // 1. 构建查询条件
        LambdaQueryWrapper<AdminUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getKeyword()), 
                     AdminUser::getUsername, query.getKeyword());
        
        // 2. 分页查询
        Page<AdminUser> page = new Page<>(query.getPage(), query.getSize());
        Page<AdminUser> result = adminUserMapper.selectPage(page, wrapper);
        
        // 3. 转换VO
        List<AdminUserVO> voList = result.getRecords().stream()
            .map(this::toVO)
            .collect(Collectors.toList());
        
        // 4. 返回结果
        return new PageResult<>(result.getTotal(), voList);
    }
    
    private AdminUserVO toVO(AdminUser entity) {
        // 实体转VO
        return BeanUtil.copyProperties(entity, AdminUserVO.class);
    }
}
```

**Controller层模板:**
```java
@RestController
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class AdminUserController {
    
    private final AdminUserService adminUserService;
    
    @OperationLog("查询管理员列表")
    @GetMapping("/list")
    public Result<PageResult<AdminUserVO>> list(@Validated AdminUserQuery query) {
        return Result.success(adminUserService.list(query));
    }
}
```

### 3. 通用类

**分页结果类:**
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> implements Serializable {
    private Long total;
    private List<T> list;
}
```

**分页查询基类:**
```java
@Data
public class PageQuery implements Serializable {
    
    @Min(value = 1, message = "页码最小为1")
    private Integer page = 1;
    
    @Min(value = 1, message = "每页数量最小为1")
    @Max(value = 100, message = "每页数量最大为100")
    private Integer size = 10;
}
```

---

## 快速开发工具

### 1. MyBatis-Plus代码生成器

```java
public class CodeGenerator {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/community_admin", "root", "password")
            .globalConfig(builder -> {
                builder.author("Community Team")
                    .outputDir(System.getProperty("user.dir") + "/src/main/java");
            })
            .packageConfig(builder -> {
                builder.parent("com.community.admin")
                    .entity("entity")
                    .mapper("mapper")
                    .service("service")
                    .serviceImpl("service.impl")
                    .controller("controller");
            })
            .strategyConfig(builder -> {
                builder.addInclude("admin_user", "admin_role");
            })
            .execute();
    }
}
```

### 2. 前端代码生成器

可以使用在线工具生成CRUD页面:
- Element Plus Admin: https://element-plus-admin.cn/
- Vue Vben Admin: https://vvbin.cn/next/

---

## 测试策略

### 1. 单元测试示例

```java
@SpringBootTest
class AdminUserServiceTest {
    
    @Autowired
    private AdminUserService adminUserService;
    
    @Test
    void testList() {
        AdminUserQuery query = new AdminUserQuery();
        query.setPage(1);
        query.setSize(10);
        
        PageResult<AdminUserVO> result = adminUserService.list(query);
        
        assertNotNull(result);
        assertTrue(result.getTotal() >= 0);
    }
}
```

### 2. 接口测试

使用Postman或编写测试用例:
```java
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AdminUserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void testList() throws Exception {
        mockMvc.perform(get("/admin/user/list")
                .param("page", "1")
                .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200));
    }
}
```

---

## 性能优化建议

### 1. 数据库优化
- 合理使用索引
- 避免N+1查询
- 使用连接查询代替多次查询
- 分页查询优化

### 2. 缓存优化
- 热点数据缓存(Redis)
- 缓存失效策略
- 缓存穿透防护

### 3. 代码优化
- 避免在循环中查询数据库
- 使用批量操作
- 异步处理耗时操作

---

## 常见问题

### Q1: 如何添加新的API接口?
1. 在Controller中添加方法
2. 在Service中实现业务逻辑
3. 添加@OperationLog注解记录日志
4. 更新API文档

### Q2: 如何添加新的权限?
1. 在数据库admin_permission表中添加记录
2. 关联到相应的角色(admin_role_permission)
3. 在接口上添加@PreAuthorize("hasAuthority('permission:code')")

### Q3: 如何扩展新的业务模块?
参考现有的认证模块结构:
1. 创建Entity
2. 创建Mapper(接口+XML)
3. 创建Service(接口+实现)
4. 创建Controller
5. 创建DTO
6. 编写单元测试

---

## 下一步行动

### 立即可做:
1. ✅ 实现系统管理模块(管理员、角色、权限CRUD)
2. ✅ 完善操作日志查询功能
3. ✅ 编写单元测试

### 短期目标(1-2周):
4. ✅ 实现用户管理模块
5. ✅ 实现订单管理模块
6. ✅ 前端登录页面开发

### 中期目标(3-4周):
7. ✅ 完成前端所有页面
8. ✅ 集成测试
9. ✅ 性能优化

---

**建议**: 优先完成系统管理模块,因为它是管理其他模块的基础!

**参考代码**: 认证模块的代码可以作为其他模块的开发模板。
